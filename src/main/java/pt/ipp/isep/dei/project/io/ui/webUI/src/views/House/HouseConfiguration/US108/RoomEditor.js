import React from 'react';
import 'react-day-picker/lib/style.css';import US108Put from "./US108Put";
import US108BackButton from "./US108BackButton";
import {confirmAlert} from "react-confirm-alert";
import {Button} from "reactstrap";

class RoomEditor extends React.Component {

  constructor(props) {
    super(props);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.state = {
      isHidden: true,
      name: this.props.name,
      floor: '',
      width: '',
      length: '',
      height: '',
      link: this.props.link
    };
  }

  handleInputChange = attribute => event => {
    this.setState({
      [attribute]: event.target.value,
      isHidden: true
    });
  };


  handleSubmit() {
    this.props.onFetchSensor(this.state);
  }

  submit = () => {
    confirmAlert({
      title: 'Confirm your room configuration',
      message: 'The room has the following new configuration: Floor: ' + this.props.floor + '. | Width: ' + this.props.width + 'm. | Length: ' + this.props.length + 'm. | Height: ' + this.props.height + 'm. Do you want to proceed?',
      buttons: [
        {
          label: 'Yes',
          onClick: () => this.toggleHidden()
        },
        {
          label: 'No',
          onClick: () => {
          }
        }
      ]
    });
  };

  toggleHidden = () => this.setState({isHidden: false})


  render() {
    const {floor, width, length, height} = this.state;
    return (
      <div>
        <label>Floor:
          <input value={floor} type="number" name="sensorId" placeholder="Floor"
                 onChange={this.handleInputChange('floor')}/>
        </label>
        <p></p>
        <label>Width:
          <input value={width} type="number" min="0" name="width" placeholder="Width"
                 onChange={this.handleInputChange('width')}/>
        </label>
        <p></p>
        <label>Length:
          <input value={length} type="number" min="0" name="length" placeholder="Length"
                 onChange={this.handleInputChange('length')}/>
        </label>
        <p></p>
        <label>Height:
          <input value={height} type="number" min="0" name="height" placeholder="Height"
                 onChange={this.handleInputChange('height')}/>
        </label>
        <p></p>
        <Button style={{backgroundColor: '#e4e5e6', marginBottom: '1rem'}} onClick={(event) => {
          this.submit();
        }}>Edit the
          room {this.props.name}</Button><US108BackButton/>
        {this.state.isHidden === false ?
          <US108Put name={this.props.name} floor={this.state.floor} width={this.state.width} length={this.state.length}
                    height={this.state.height} link={this.props.link}/>:''}
      </div>
    )
  }
}

export default RoomEditor
