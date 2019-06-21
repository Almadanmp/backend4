import React from 'react';
import 'react-day-picker/lib/style.css';
import {inactivateSensorFromArea} from "./Actions010";
import {connect} from 'react-redux';
import {confirmAlert} from 'react-confirm-alert'; // Import
import 'react-confirm-alert/src/react-confirm-alert.css';


class AreaSensorInactivation extends React.Component {

  constructor(props) {
    super(props);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.state = {
      id: 0,
      sensorId: '',
      isChecked: false
    };

    this.handleInputChange = attribute => event => {
      this.setState({
        [attribute]: event.target.value
      });
    };

  }

  componentWillMount () {
    this.setState( { isChecked: this.props.isChecked } );
  }

  submit = () => {
    this.setState( { isChecked: true } );
    confirmAlert({
      title: 'Confirm inactivation',
      message: 'Are you sure to inactivate ' + this.props.sensorId +'?',
      buttons: [
        {
          label: 'Yes',
          onClick: () => this.handleSubmit()
        },
        {
          label: 'No',
          onClick: () => {
            this.setState( { isChecked: false } );

          }
        }
      ]
    });
  };

  handleSubmit() {
    const{link}=this.props.link.href;
    this.props.onInactivateSensorFromArea(link);
  }


  render() {
    const isEnabled = this.state.isChecked != null;
    return (
      <>

        <div className="switch-container">
          <label>
            <input ref="switch" checked={this.state.isChecked } disabled={isEnabled} onChange={ this.submit } className="switch" type="checkbox" />
            <div>

              <div> </div>
            </div>
          </label>
        </div>
      </>
    )
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    onInactivateSensorFromArea: (link) => {
      dispatch(inactivateSensorFromArea(link))
    }
  }
};


export default connect(null, mapDispatchToProps)(AreaSensorInactivation);
